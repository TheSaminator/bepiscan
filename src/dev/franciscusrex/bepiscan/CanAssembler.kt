package dev.franciscusrex.bepiscan

import javafx.scene.transform.Rotate

object CanAssembler {
	const val CAN_SCALE = 3.0
	
	fun assembleCan() = Group3D(
		ViewManager.texturedMesh(ModelManager.canBody, TextureManager.canTexture),
		ViewManager.texturedMesh(ModelManager.canTopLip, TextureManager.lipTexture),
		ViewManager.texturedMesh(ModelManager.canBottomLip, TextureManager.lipTexture),
		ViewManager.texturedMesh(ModelManager.canTopCap, TextureManager.topCapTexture),
		ViewManager.texturedMesh(ModelManager.canBottomCap, TextureManager.bottomCapTexture).apply {
			rotationAxis = Rotate.Z_AXIS
			rotate = 180.0
		}
	).apply {
		location.y = -(ModelManager.TOTAL_HEIGHT / 2) * CAN_SCALE
		
		scale.x = CAN_SCALE
		scale.y = CAN_SCALE
		scale.z = CAN_SCALE
	}
}
