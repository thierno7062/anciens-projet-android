/**
 * Created by admin on 02/01/2017.
 */

var gulp = require('gulp'),
    rename = require('gulp-rename'),
    plumber = require('gulp-plumber'),
    inject = require('gulp-inject-string');
var config = require('./config');
var handleErrors = require('./handle-errors');
module.exports = {
    app: app
}


function app() {
    var stream = null;//gulp.src(config.app + 'index.html')
        //.pipe(inject.after('</script>-->', '\n\t<script src="https://maps.googleapis.com/maps/api/js?libraries=places&key='+config.googlePlaceKey+'"></script>'))
        //.pipe(plumber({errorHandler: handleErrors}))
        //.pipe(gulp.dest(config.app));

    return stream;
}

