package dev.franciscusrex.bepiscan

import javafx.scene.image.Image

object TextureManager {
	val canTexture = Image(javaClass.getResourceAsStream("/can.png"))
	val lipTexture = Image(javaClass.getResourceAsStream("/lip.png"))
	val topCapTexture = Image(javaClass.getResourceAsStream("/topCap.png"))
	val bottomCapTexture = Image(javaClass.getResourceAsStream("/bottomCap.png"))
}
