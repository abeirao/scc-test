'use strict';

/***
 * Exported functions to be used in the testing scripts.
 */
module.exports = {
  uploadImageBody,
  processUploadReply,
  selectImageToDownload,
  postEntity,
  processPostReply,
  selectEntity
}


const fs = require('fs')
const fetch = require('node-fetch')

var imagesIds = []
var images = []
var entitiesIds = []
var entities = []
var forumIds = []
var forums = []
var calendarIds = []
var calendars = []
var reservationIds = []
var reservations = []

// All endpoints starting with the following prefixes will be aggregated in the same for the statistics
var statsPrefix = [ ["/media/","GET"]]

// Function used to compress statistics
global.myProcessEndpoint = function( str, method) {
	var i = 0;
	for( i = 0; i < statsPrefix.length; i++) {
		if( str.startsWith( statsPrefix[i][0]) && method == statsPrefix[i][1])
			return method + ":" + statsPrefix[i][0];
	}
	return method + ":" + str;
}

// Auxiliary function to select an element from an array
Array.prototype.sample = function(){
	   return this[Math.floor(Math.random()*this.length)]
}

// Returns a random value, from 0 to val
function random( val){
	return Math.floor(Math.random() * val)
}

// Loads data about images from disk
function loadImage() {
	var i
	var basefile
	if( fs.existsSync( '/images'))
		basefile = '/images/cats.'
	else
		basefile =  'images/cats.'
	for( i = 1; i <= 40 ; i++) {
		var img  = fs.readFileSync(basefile + i + '.jpeg')
		images.push( img)
	}
}

function loadEntities() {
	var entitie =
  {
    "_rid": "0",
    "id": "0",
    "name": "mcdonalds",
    "description": "very nice",
    "mediaIds": [],
    "calendarIds": [],
    "listed": "true",

  }
}

function loadForums() {
  var forum =
  {
    "_rid":"0",
    "id":"0",
    "messages": [],
    "entityId":"0",

  }
  forums.push(forum)
}

function loadCalendars() {
  var calendar =
  {
    "_rid":"0",
    "id":"0",
    "name":"0",
    "description":"0",
    "availableDays":["21/10/2020"],
    "reservations": []
    "calendarEntry": []

  }
  calendars.push(calendar)
}

function loadReservations() {
  var reservation =
  {
    "employees":[
      {"firstName":"John", "lastName":"Doe"},
      {"firstName":"Anna", "lastName":"Smith"},
      {"firstName":"Peter", "lastName":"Jones"}
    ]
  }
  reservations.push(reservation)

}

loadImages();
loadEntities();
loadForums();
loadCalendars();
loadReservations();

/**
 * Sets the body to an image, when using images.
 */
function uploadImageBody(requestParams, context, ee, next) {
	requestParams.body = images.sample()
	return next()
}

/**
 * Process reply of the download of an image.
 * Update the next image to read.
 */
function processUploadReply(requestParams, response, context, ee, next) {
	if( typeof response.body !== 'undefined' && response.body.length > 0) {
		imagesIds.push(response.body)
	}
    return next()
}

/**
 * Select an image to download.
 */
function selectImageToDownload(context, events, done) {
	if( imagesIds.length > 0) {
		context.vars.imageId = imagesIds.sample()
	} else {
		delete context.vars.imageId
	}
	return done()
}

// TODO

function postEntity(requestParams, context) {
	requestParams.body = entities.sample()
	return next()
}



function processPostReply(requestParams, response, context) {
	if( typeof response.body !== 'undefined' && response.body.length > 0) {
		entitiesIds.push(response.body)
	}
    return next()

}

function selectEntity(context, events, done) {
	if(entitiesIds.length > 0) {
		context.vars.entityId = entitiesIds.sample()
	}
	else {
		delete context.vars.entityId
	}
	return done()

}
