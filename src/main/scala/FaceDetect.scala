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
  val faceDetector = new CascadeClassifier("data/haarcascade_frontalface_alt.xml")
  val eyesDetector = new CascadeClassifier("data/haarcascade_eye.xml")
  val noseDetector = new CascadeClassifier("data/haarcascade_mcs_nose.xml")

  val faceDetections = new RectVector()
  val eyesDetections = new RectVector()
  val noseDetections = new RectVector()

  faceDetector.detectMultiScale(equalizedMat, faceDetections)

  for {
    i <- 0 until faceDetections.size().toInt
    rect = faceDetections.get(i)
  }{
    rectangle(imageRaw, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 0, 255))
  }

  eyesDetector.detectMultiScale(equalizedMat, eyesDetections)

  for {
    i <- 0 until eyesDetections.size().toInt
    rect = eyesDetections.get(i)
  }{
    rectangle(imageRaw, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255, 0))
  }

  eyesDetector.detectMultiScale(equalizedMat, noseDetections)

  for {
    i <- 0 until noseDetections.size().toInt
    rect = noseDetections.get(i)
  }{
    rectangle(imageRaw, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 255, 0))
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

  val j2dconverter = new Java2DFrameConverter
  val opencvConverter = new OpenCVFrameConverter.ToMat()

  def loadMatImage(strBase64: String):Mat = {
    //    val bytes = pic.picture.toByteArray
    val bytes = strBase64.toByteArray
    val img: BufferedImage =  ImageIO.read(new ByteArrayInputStream(bytes))

    opencvConverter.convert(j2dconverter.convert(img))
  }

}


