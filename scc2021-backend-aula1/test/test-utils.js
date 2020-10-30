'use strict';

/***
 * Exported functions to be used in the testing scripts.
 */
module.exports = {
  uploadImageBody,
  processUploadReply,
  selectImageToDownload,
  postEntity,
  processPostEntityReply,
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
  entities.push(entity)
  reservationIds.push(reservation.id)
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
  forumIds.push(forum.id)
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
  calendarsIds.push(calendar.id)
}

function loadReservations() {
  var reservation =
  {
    "_rid":"0",
    "id":"0",
    "name":"lunch",
    "day":"monday",
    "entityId":"0",
    "media":"media",
  }
  reservations.push(reservation)
  reservationIds.push(reservation.id)

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

// entities

function postEntity(requestParams, context) {
	requestParams.body = entities.sample()
	return next()
}


function processPostEntityReply(requestParams, response, context) {
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

// forums

function postForum(requestParams, context) {
	requestParams.body = forums.sample()
	return next()
}

function processPostForumReply(requestParams, response, context) {
	if( typeof response.body !== 'undefined' && response.body.length > 0) {
		forumIds.push(response.body)
	}
    return next()
}


function selectForum(context, events, done) {
	if(forumIds.length > 0) {
		context.vars.forumId = forumIds.sample()
	}
	else {
		delete context.vars.forumId
	}
	return done()

}


// Calendars

function postCalendar(requestParams, context) {
	requestParams.body = calendars.sample()
	return next()
}

function processPostCalendarReply(requestParams, response, context) {
	if( typeof response.body !== 'undefined' && response.body.length > 0) {
		calendarIds.push(response.body)
	}
    return next()
}


function selectCalendar(context, events, done) {
	if(calendarIds.length > 0) {
		context.vars.calendarId = calendarIds.sample()
	}
	else {
		delete context.vars.calendarId
	}
	return done()

}


// Reservations

function postReservation(requestParams, context) {
	requestParams.body = reservations.sample()
	return next()
}

function processPostReservationReply(requestParams, response, context) {
	if( typeof response.body !== 'undefined' && response.body.length > 0) {
		reservationIds.push(response.body)
	}
    return next()
}


function selectCalendar(context, events, done) {
	if(reservationIds.length > 0) {
		context.vars.reservationId = reservationIds.sample()
	}
	else {
		delete context.vars.reservationId
	}
	return done()

}



