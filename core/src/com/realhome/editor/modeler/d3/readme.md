# How to convert House to Libgdx Model

## 1 - Try directly in the builder

```Pseudo-Code

main:
	create house node and put it in model

	for floor in floors:
		create floor node and put it in house node

		create walls node and put it in floor node
		for wall in floor's wall:
			create wall node and put it in walls node

			faces = getFacesWall(wall)

			for face in faces:

				get material in model
				if material is null:
					create material

				meshpart = builder.addInMesh(face.vertices, face.indices)

				create nodepart with meshpart and material
				add nodepart to wall node




# Return an array of face
# face contains vertex info and material
getFacesWall(wall):
	# p4,5,6,7 = p0,1,2,3 but at wall top
	points = computer.extractWall3d(wall)
	p0linked = computer.isLinkedPoint(wall, 0)
	p1linked = computer.isLinkedPoint(wall, 1)

	if not p0linked:
		create face p0,1,4,5
	if not p1linked:
		create face p2,3,6,7

	create face p1,3,p5,7
	...

	return faces

```