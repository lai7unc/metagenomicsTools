
// modified from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Inheritance_and_the_prototype_chain

'use strict';

class Polygon {
	
	// this would be ok in java, but 
	// in javascript all variables have to be declared 
	// in the constructor
	//this.someVal = 5;

  constructor(height, width) {
    this.height = height;
    this.width = width;
  }
}

class Square extends Polygon {
  constructor(sideLength) {
    super(sideLength, sideLength);
  }
  get area() {
    return this.height * this.width;
  }
  set sideLength(newLength) {
    this.height = newLength;
    this.width = newLength;
  }
}

var square1 = new Square(2);
var square2 = new Square(3);

// works as in java (with setters and getters formalized as parameters
console.log( square1.area +  " " + square2.area)
square2.sideLength=10;
console.log( square1.area +  " " + square2.area)


// Object.create bypasses the constructor 
// (Java would not allow you do instantiate an object without invoking constructor)
var square3 = Object.create(Square)
console.log( square3.area)

console.log( square1.someVal+  " " + square2.someVal)

// javascript is sort of a simulation of a strongly typed language
// rather that a truly strongly typed language
var Car = function()
{
	this.doors = 4;
	this.make = "Chevy"
		
	this.getNumDoors = function() { return this.doors}
}

// it's really all about what happens to this
// and what the scope of this is
// rather than the type the way it is in Java
Car.call(square1)

// square1 has picked up the properties of a Car
// from the Car constructor
console.log(square1)
console.log(square1.getNumDoors())
