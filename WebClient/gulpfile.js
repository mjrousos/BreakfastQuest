"use strict"

const gulp = require('gulp');
const babel = require('gulp-babel'); // Transpile our server JS
const cleanCSS = require('gulp-clean-css'); // Minifies CSS
const concat = require('gulp-concat'); // Concatenates files
const lint = require('gulp-eslint');  // Lint JS files (including JSX)
const imagemin = require('gulp-imagemin'); // Minify SVG files
const sourcemaps = require('gulp-sourcemaps'); // Create source maps for CSS
const rimraf = require('rimraf'); // Deletes things
const webpack = require('webpack'); // Bundles JS and other resources

const eslintConfig = require('./.eslintrc');

// Configure paths, globs, etc.
var config = {
  paths: {
    clientSrc: './src/client',
    serverSrc: './src/server',
    dist: './dist',
    css: [
      'node_modules/bootstrap/dist/css/bootstrap.min.css',
      'node_modules/bootstrap/dist/css/bootstrap-theme.min.css',
      'node_modules/toastr/package/toastr.css'
    ]
  }
};
config.paths.clientDist = config.paths.dist + "/public";
config.paths.html = config.paths.clientSrc + '/**/*.html';
config.paths.css.push(config.paths.clientSrc + '/**/*.css');
config.paths.images = config.paths.clientSrc + '/images/**/*';
config.paths.imagesOut = config.paths.clientDist + '/img';
config.paths.clientJs = config.paths.clientSrc + '/**/*.js';
config.paths.serverJs = config.paths.serverSrc + '/**/*.js';
config.paths.serverConfig = config.paths.serverSrc + '/**/*.json';
config.paths.js = [config.paths.clientJs, config.paths.serverJs];

// Delete /dist
gulp.task('clean', function (cb) {
  // Note that rimraf requires a callback to be passed in
  rimraf(config.paths.dist, cb);
});

// Minify and concat CSS into /dist/public/bundle.css
gulp.task('build-css', ['clean'], function () {
  gulp.src(config.paths.css)
    .pipe(sourcemaps.init())
    .pipe(cleanCSS({ compatibility: 'ie8' }))
    .pipe(concat('bundle.css'))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest(config.paths.clientDist));
});

// Minify images into /dist/public/img
gulp.task('build-images', ['clean'], function () {
  return gulp.src(config.paths.images)
    .pipe(imagemin())
    .pipe(gulp.dest(config.paths.imagesOut));
});

// Copy html int /dist/public
gulp.task('build-html', ['clean'], function () {
  gulp.src(config.paths.html)
    .pipe(gulp.dest(config.paths.clientDist));
});

// Use webpack to pack client js into /dist/public
gulp.task('build-client-js', ['clean'], function (cb) {
  webpack(require('./webpack.config.js'), function webpackCallback(err, stats) {
    if (err) {
      return cb(err);
    }

    if (stats.compilation.errors && stats.compilation.errors.length) {
      console.error(stats.compilation.errors);
      return cb(new Error('Webpack failed to compile the application.'));
    }
    else {
      console.log('Webpack succeeded');
    }

    cb();
  });
});

// Copy json config files to /dist
gulp.task('build-server-config', ['clean'], function () {
  gulp.src(config.paths.serverConfig)
    .pipe(gulp.dest(config.paths.dist));
});

// Transpile server JS files and concat into /dist/app.js
gulp.task('build-server-js', ['clean'], function () {
  return gulp.src(config.paths.serverJs)
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(concat('app.js'))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest(config.paths.dist));
});

// Lint all js
gulp.task('lint', function () {
  return gulp.src(config.paths.js)
    .pipe(lint(eslintConfig))
    .pipe(lint.format());    // Show results in console
});

// Watch files and re-build if any files
// TODO - If build begins to take too long,
//        create separate clean tasks so that each build step (build-css, build-js, etc.)
//        can run independently.
gulp.task('watch', function () {
  gulp.watch(config.paths.html, ['build']);
  gulp.watch(config.paths.js, ['build', 'lint']);
  gulp.watch(config.paths.css, ['build']);
  gulp.watch(config.paths.images, ['build']);
  gulp.watch(config.paths.serverConfig, ['build']);
});

gulp.task('build', ['build-images',
  'build-css',
  'build-html',
  'build-client-js',
  'build-server-js',
  'build-server-config']);

gulp.task('default', ['build', 'lint', 'watch']);
