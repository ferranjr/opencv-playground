import com.ferranjr.utils.Viewer._

import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgcodecs._
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacpp.opencv_objdetect._


object FaceDetect extends App {

  val imageFilename = "data/skyfall.jpg"
  val imageRaw = cvLoadImage(imageFilename)

  val grayImage: IplImage = cvCreateImage(cvGetSize(imageRaw), IPL_DEPTH_8U, 1)
  cvCvtColor(imageRaw, grayImage, CV_BGR2GRAY)

  val histImage: IplImage = cvCreateImage(cvGetSize(imageRaw), IPL_DEPTH_8U, 1)
  cvEqualizeHist(grayImage, histImage)

//  val classifier = new CvHaarClassifierCascade(cvLoad("data/haarcascade_frontalface_alt.xml"))
//  val storage = new CvMemStorage()

//  val faces = cvHaarDetectObjects(histImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING)
//
//  val faceRects = new Rect()
//  faceCascade.detectMultiScale( histImage.asCvMat, faceRects)

  /*
    val faceXml = FaceDetectorApp.getClass.getClassLoader.getResource("haarcascade_frontalface_alt.xml").getPath
    val faceCascade = new CascadeClassifier(faceXml)
    val faceRects = new Rect() // will hold the rectangles surrounding the detected faces
    faceCascade.detectMultiScale(equalizedMat, faceRects)
   */
  showInCanvas(histImage)

}