package dev.franciscusrex.bepiscan

import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.CullFace
import javafx.scene.shape.MeshView
import javafx.scene.shape.TriangleMesh

object ViewManager {
	fun texturedMesh(mesh: TriangleMesh, texture: Image): Node {
		return MeshView(mesh).apply {
			cullFace = CullFace.NONE
			
			material = PhongMaterial().apply {
				diffuseMap = texture
				specularColor = Color.web("#AAAAAA")
			}
		}
	}
}
