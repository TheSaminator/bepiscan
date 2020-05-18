package dev.franciscusrex.bepiscan

import javafx.scene.DepthTest
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.transform.Rotate
import javafx.scene.transform.Scale
import javafx.scene.transform.Translate

class Group3D(vararg children: Node) : Group(*children) {
	val location = Translate()
	val pivotLoc = Translate()
	
	val rotationX = Rotate().apply {
		axis = Rotate.X_AXIS
		
		pivotXProperty().bind(pivotLoc.xProperty())
		pivotYProperty().bind(pivotLoc.yProperty())
		pivotYProperty().bind(pivotLoc.zProperty())
	}
	
	val rotationY = Rotate().apply {
		axis = Rotate.Y_AXIS
		
		pivotXProperty().bind(pivotLoc.xProperty())
		pivotYProperty().bind(pivotLoc.yProperty())
		pivotYProperty().bind(pivotLoc.zProperty())
	}
	
	val rotationZ = Rotate().apply {
		axis = Rotate.Z_AXIS
		
		pivotXProperty().bind(pivotLoc.xProperty())
		pivotYProperty().bind(pivotLoc.yProperty())
		pivotYProperty().bind(pivotLoc.zProperty())
	}
	
	val scale = Scale().apply {
		pivotXProperty().bind(pivotLoc.xProperty())
		pivotYProperty().bind(pivotLoc.yProperty())
		pivotYProperty().bind(pivotLoc.zProperty())
	}
	
	init {
		depthTest = DepthTest.ENABLE
		
		transforms.addAll(location, scale, rotationZ, rotationY, rotationX)
	}
}
