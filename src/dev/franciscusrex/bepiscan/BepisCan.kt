package dev.franciscusrex.bepiscan

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.*
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.tanh

class BepisCan : Application() {
	override fun start(stage: Stage) {
		stage.title = "Bepis Can"
		
		stage.scene = Scene(
			StackPane().also { root ->
				val camera = PerspectiveCamera(true).apply {
					translateZ = -22.0
					fieldOfView = 50.0
					nearClip = 0.1
					farClip = 10000.0
				}
				
				val flashlight = PointLight(Color.web("#AAAAAA")).apply {
					translateZ = -22.0
				}
				
				val cg = Group3D().also { cg ->
					cg.children.add(camera)
					cg.children.add(flashlight)
					
					var zoomLevel = 0.0
					
					root.onScroll = EventHandler { e ->
						zoomLevel += e.deltaY / 40.0
						
						zoomLevel = zoomLevel.coerceIn(-15.0..15.0)
						
						camera.translateZ = (tanh(zoomLevel / 5) * 10) - 22
					}
					
					val angleMult = 200.0
					
					var prevX = 0.0
					var prevY = 0.0
					
					root.onMouseDragged = EventHandler { e ->
						val normalX = e.sceneX / stage.scene.width
						val normalY = e.sceneY / stage.scene.height
						
						cg.rotationY.angle += (normalX - prevX) * angleMult
						cg.rotationX.angle -= (normalY - prevY) * angleMult
						
						cg.rotationX.angle = cg.rotationX.angle.coerceIn(-90.0..90.0)
						
						prevX = normalX
						prevY = normalY
					}
					
					root.onMouseMoved = EventHandler { e ->
						prevX = e.sceneX / stage.scene.width
						prevY = e.sceneY / stage.scene.height
					}
				}
				
				val ss = SubScene(Group().also { root3d ->
					root3d.children.addAll(
						AmbientLight(Color.web("#555555")),
						cg,
						CanAssembler.assembleCan()
					)
				}, 1280.0, 720.0, true, SceneAntialiasing.BALANCED)
				
				ss.depthTest = DepthTest.ENABLE
				ss.fill = Color.web("#77bbff")
				ss.camera = camera
				
				root.children.add(ss)
			}
		).apply {
			(root.childrenUnmodifiable.single { it is SubScene } as SubScene).let { ss ->
				ss.widthProperty().bind(widthProperty())
				ss.heightProperty().bind(heightProperty())
			}
		}
		
		stage.show()
	}
}
