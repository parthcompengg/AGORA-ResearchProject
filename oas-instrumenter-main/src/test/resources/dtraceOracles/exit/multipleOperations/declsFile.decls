decl-version 2.0
var-comparability implicit

ppt main.operation1:::CLASS
ppt-type class

ppt main.operation1.operation1&200(main.operation1&Input):::ENTER
ppt-type enter
variable input
	var-kind variable
	dec-type main.operation1&Input
	rep-type hashcode
variable input.parameterOperation1
	var-kind field parameterOperation1
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String

ppt main.operation1.operation1&200&arrayOfObjectsPropertyOperation1(main.operation1&Input):::ENTER
ppt-type enter
variable input
	var-kind variable
	dec-type main.operation1&Input
	rep-type hashcode
variable input.parameterOperation1
	var-kind field parameterOperation1
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String

ppt main.operation1.operation1&200(main.operation1&Input):::EXIT1
ppt-type subexit
variable input
	var-kind variable
	dec-type main.operation1&Input
	rep-type hashcode
variable input.parameterOperation1
	var-kind field parameterOperation1
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String
variable return
	var-kind return
	dec-type main.operation1&Output&200
	rep-type hashcode
variable return.stringPropertyOperation1
	var-kind field stringPropertyOperation1
	enclosing-var return
	dec-type java.lang.String
	rep-type java.lang.String
variable return.arrayOfObjectsPropertyOperation1
	var-kind field arrayOfObjectsPropertyOperation1
	enclosing-var return
	dec-type main.arrayOfObjectsPropertyOperation1[]
	rep-type hashcode
variable return.arrayOfObjectsPropertyOperation1[..]
	var-kind array
	enclosing-var return.arrayOfObjectsPropertyOperation1
	array 1
	dec-type main.arrayOfObjectsPropertyOperation1[]
	rep-type hashcode[]

ppt main.operation1.operation1&200&arrayOfObjectsPropertyOperation1(main.operation1&Input):::EXIT2
ppt-type subexit
variable input
	var-kind variable
	dec-type main.operation1&Input
	rep-type hashcode
variable input.parameterOperation1
	var-kind field parameterOperation1
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String
variable return
	var-kind return
	dec-type main.operation1&Output&200&arrayOfObjectsPropertyOperation1
	rep-type hashcode
variable return.property1Operation1
	var-kind field property1Operation1
	enclosing-var return
	dec-type java.lang.String
	rep-type java.lang.String


ppt main.operation2:::CLASS
ppt-type class

ppt main.operation2.operation2&200(main.operation2&Input):::ENTER
ppt-type enter
variable input
	var-kind variable
	dec-type main.operation2&Input
	rep-type hashcode
variable input.parameterOperation2
	var-kind field parameterOperation2
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String

ppt main.operation2.operation2&200(main.operation2&Input):::EXIT3
ppt-type subexit
variable input
	var-kind variable
	dec-type main.operation2&Input
	rep-type hashcode
variable input.parameterOperation2
	var-kind field parameterOperation2
	enclosing-var input
	dec-type java.lang.String
	rep-type java.lang.String
variable return
	var-kind return
	dec-type main.operation2&Output&200
	rep-type hashcode
variable return.stringPropertyOperation2
	var-kind field stringPropertyOperation2
	enclosing-var return
	dec-type java.lang.String
	rep-type java.lang.String

