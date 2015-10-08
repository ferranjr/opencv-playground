package com.ferranjr.utils

import javax.swing.JFrame

import org.bytedeco.javacpp.opencv_core.{Mat, IplImage}
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat
import org.bytedeco.javacv.{OpenCVFrameConverter, CanvasFrame}


trait Viewer[T] {
  def render(in: T):Unit
}

object Viewer {

  implicit val instanceIplImage = new Viewer[IplImage]{
    def render(in: IplImage): Unit = {
      val canvas:CanvasFrame = new CanvasFrame("Window", 1)
      canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)
      canvas.setCanvasSize( in.width(), in.height() )
      val converter = new OpenCVFrameConverter.ToIplImage()
      canvas.showImage(converter.convert(in))
    }
  }

  implicit val instanceMat = new Viewer[Mat] {
    def render(in: Mat): Unit = {
      val converter = new ToMat()
      val canvas = new CanvasFrame("title image", 1)
      canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      canvas.showImage(converter.convert(in))
    }
  }

  def showInCanvas[T : Viewer](in: T):Unit = implicitly[Viewer[T]].render(in)
}