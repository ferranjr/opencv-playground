import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

import com.ferranjr.utils.Viewer._

import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgcodecs._
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacpp.opencv_objdetect._
import org.bytedeco.javacv.{Java2DFrameConverter, OpenCVFrameConverter}
import com.Base64._

import scala.io.Source

object FaceDetect extends App {

  val imageRaw = DetectionTools.loadMatImage(Source.fromFile("data/image64.txt").getLines().mkString)

  // FACE DETECTION
  val greyMat = new Mat()
  cvtColor( imageRaw, greyMat, CV_BGR2GRAY, 1)

  // Histrogram equalization
  val equalizedMat = new Mat()
  equalizeHist(greyMat, equalizedMat)


// load classifier
  List("haarcascade_frontalface_alt.xml", "haarcascade_eye.xml", "haarcascade_mcs_nose.xml")
    .foreach{ filename =>
      DetectionTools
        .detectObjectWithHaarCascade(
          s"data/$filename", equalizedMat, imageRaw
        )
    }


  // ADDING OVERLAY

  //  val imageFilename = "data/skyfall.jpg"
  val overlayFilename = "data/doggy.png"

  //  val imageRaw  = imread(imageFilename, CV_LOAD_IMAGE_COLOR)
  val mask      = imread(overlayFilename, CV_LOAD_IMAGE_GRAYSCALE)
  val overlay   = imread(overlayFilename, CV_LOAD_IMAGE_COLOR)

  val overlaySize = new Size(200, 200)

  val resizedOverlay  = new Mat()
  resize( overlay, resizedOverlay, overlaySize)

  val resizedMask     = new Mat()
  resize( mask, resizedMask, overlaySize)

  val imageROI = imageRaw(new Rect(imageRaw.cols - resizedOverlay.cols, imageRaw.rows - resizedOverlay.rows, resizedOverlay.cols, resizedOverlay.rows))
  resizedOverlay.copyTo(imageROI, resizedMask)


//
//  // MUSTACHES
//  val mustacheFilename = "data/mustache.png"
//
//  val mustacheRaw = imread(mustacheFilename)
//
//
//
  showInCanvas(imageRaw)

}

object DetectionTools {

  private val j2dconverter    = new Java2DFrameConverter
  private val opencvConverter = new OpenCVFrameConverter.ToMat()

  def loadMatImage(strBase64: String):Mat = {
    //    val bytes = pic.picture.toByteArray
    val bytes = strBase64.toByteArray
    val img: BufferedImage =  ImageIO.read(new ByteArrayInputStream(bytes))

    opencvConverter.convert(j2dconverter.convert(img))
  }


  def detectObjectWithHaarCascade(
                                   cascadeClassifierFileName: String,
                                   equalizedMat: Mat,
                                   imageRaw: Mat
                                 ):Unit = {

    val detector = new CascadeClassifier(cascadeClassifierFileName)
    val detections = new RectVector()

    detector.detectMultiScale(equalizedMat, detections)

    for{
      i <- 0 until detections.size().toInt
      rect = detections.get(i)
    }{
      rectangle(imageRaw, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 0, 255))
    }
  }

}


