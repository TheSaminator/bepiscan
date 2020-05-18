package dev.franciscusrex.bepiscan

import javafx.scene.shape.TriangleMesh
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object ModelManager {
	const val DIVS = 32
	const val LIP_RADIUS = 0.05
	const val BODY_RADIUS = 1.0
	const val BODY_HEIGHT = 2.5
	const val SLOPE_RADIUS = 0.675
	const val SLOPE_HEIGHT = 0.325
	const val TOTAL_HEIGHT = SLOPE_HEIGHT * 2 + BODY_HEIGHT
	
	val canBody: TriangleMesh by lazy {
		val points = (0 until DIVS).flatMap { i ->
			val angle = i * 2 * PI / DIVS
			val x = cos(angle)
			val y = 0.0
			val z = sin(angle)
			
			listOf(
				x * SLOPE_RADIUS, y, z * SLOPE_RADIUS, // Bottom of bottom slope
				x * BODY_RADIUS, y + SLOPE_HEIGHT, z * BODY_RADIUS, // Bottom of body
				x * BODY_RADIUS, y + SLOPE_HEIGHT + BODY_HEIGHT, z * BODY_RADIUS, // Top of body
				x * SLOPE_RADIUS, y + TOTAL_HEIGHT, z * SLOPE_RADIUS // Top of top slope
			)
		}.map { it.toFloat() }.toFloatArray()
		
		val uv = (0..DIVS).flatMap { i ->
			val u = i.toDouble() / DIVS
			
			listOf(
				u, 0.0,
				u, (SLOPE_HEIGHT / TOTAL_HEIGHT),
				u, 1.0 - (SLOPE_HEIGHT / TOTAL_HEIGHT),
				u, 1.0
			)
		}.map { it.toFloat() }.toFloatArray()
		
		val faces = (0 until DIVS).flatMap { i ->
			val thisSegment = i * 4
			val nextSegment = (i + 1) * 4
			
			// Bottom slope:
			// t0, n0, n1
			// t0, n1, t1
			// Body:
			// t1, n1, n2
			// t1, n2, t2
			// Top slope:
			// t2, n2, n3
			// t2, n3, t3
			listOf(
				// Bottom slope
				thisSegment, nextSegment + 1, nextSegment,
				thisSegment, thisSegment + 1, nextSegment + 1,
				// Body
				thisSegment + 1, nextSegment + 2, nextSegment + 1,
				thisSegment + 1, thisSegment + 2, nextSegment + 2,
				// Top slope
				thisSegment + 2, nextSegment + 3, nextSegment + 2,
				thisSegment + 2, thisSegment + 3, nextSegment + 3
			)
		}.flatMap { listOf(it % (DIVS * 4), it) }.toIntArray()
		
		val mesh = TriangleMesh()
		mesh.points.addAll(*points)
		mesh.texCoords.addAll(*uv)
		mesh.faces.addAll(*faces)
		mesh
	}
	
	fun createLip(y: Double): TriangleMesh {
		val points = (0 until DIVS).flatMap { i ->
			val angle = i * 2 * PI / DIVS
			val x = cos(angle)
			val z = sin(angle)
			
			val dx = cos(angle) * LIP_RADIUS
			val dy = LIP_RADIUS
			val dz = sin(angle) * LIP_RADIUS
			
			listOf(
				x * SLOPE_RADIUS + dx, y, z * SLOPE_RADIUS + dz,
				x * SLOPE_RADIUS, y + dy, z * SLOPE_RADIUS,
				x * SLOPE_RADIUS - dx, y, z * SLOPE_RADIUS - dz,
				x * SLOPE_RADIUS, y - dy, z * SLOPE_RADIUS
			)
		}.map { it.toFloat() }.toFloatArray()
		
		val uv = (0 until DIVS).flatMap { i ->
			val u = i.toDouble() / DIVS
			
			listOf(
				u, 0.0,
				u, 0.5,
				u, 1.0,
				u, 0.5
			)
		}.map { it.toFloat() }.toFloatArray()
		
		val faces = (0 until DIVS).flatMap { i ->
			val thisSegment = i * 4
			val nextSegment = ((i + 1) % DIVS) * 4
			
			// Quad 1:
			// t0, n0, n1
			// t0, n1, t1
			// Quad 2:
			// t1, n1, n2
			// t1, n2, t2
			// Quad 3:
			// t2, n2, n3
			// t2, n3, t3
			// Quad 4:
			// t3, n3, n0
			// t3, n0, t0
			listOf(
				// Quad 1
				thisSegment, nextSegment + 1, nextSegment,
				thisSegment, thisSegment + 1, nextSegment + 1,
				// Quad 2
				thisSegment + 1, nextSegment + 2, nextSegment + 1,
				thisSegment + 1, thisSegment + 2, nextSegment + 2,
				// Quad 3
				thisSegment + 2, nextSegment + 3, nextSegment + 2,
				thisSegment + 2, thisSegment + 3, nextSegment + 3,
				// Quad 4
				thisSegment + 3, nextSegment, nextSegment + 3,
				thisSegment + 3, thisSegment, nextSegment
			)
		}.flatMap { listOf(it, it) }.toIntArray()
		
		val mesh = TriangleMesh()
		mesh.points.addAll(*points)
		mesh.texCoords.addAll(*uv)
		mesh.faces.addAll(*faces)
		return mesh
	}
	
	val canBottomLip: TriangleMesh by lazy {
		createLip(TOTAL_HEIGHT)
	}
	
	val canTopLip: TriangleMesh by lazy {
		createLip(0.0)
	}
	
	private fun createCap(y: Double): TriangleMesh {
		val points = (listOf(0.0, y, 0.0) + (0 until DIVS).flatMap { i ->
			val angle = i * 2 * PI / DIVS
			val x = cos(angle)
			val z = sin(angle)
			
			listOf(
				x * SLOPE_RADIUS, y, z * SLOPE_RADIUS
			)
		}).map { it.toFloat() }.toFloatArray()
		
		val uv = (listOf(0.5, 0.5) + (0 until DIVS).flatMap { i ->
			val angle = i * 2 * PI / DIVS
			val u = (cos(angle) + 1.0) / 2
			val v = (sin(angle) + 1.0) / 2
			
			listOf(u, v)
		}).map { it.toFloat() }.toFloatArray()
		
		val faces = (0 until DIVS).flatMap { i ->
			val thisSegment = i + 1
			val nextSegment = ((i + 1) % DIVS) + 1
			
			listOf(
				0, thisSegment, nextSegment
			)
		}.flatMap { listOf(it, it) }.toIntArray()
		
		val mesh = TriangleMesh()
		mesh.points.addAll(*points)
		mesh.texCoords.addAll(*uv)
		mesh.faces.addAll(*faces)
		return mesh
	}
	
	val canBottomCap: TriangleMesh by lazy {
		createCap(TOTAL_HEIGHT)
	}
	
	val canTopCap: TriangleMesh by lazy {
		createCap(0.0)
	}
}
