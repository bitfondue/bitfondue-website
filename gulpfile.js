var gulp = require('gulp'),
    concat = require('gulp-concat')
    sass = require('gulp-sass');

var paths = {
  sass: 'resources/sass/*.scss'
}

gulp.task('styles', function () {
  gulp.src([
    'bower_components/bootstrap/dist/css/bootstrap.min.css',
    paths.sass
  ])
  .pipe(sass().on('error', sass.logError))
  .pipe(concat('app.min.css'))
  .pipe(gulp.dest('resources/public/css'));
});

gulp.task('default', ['styles']);

gulp.task('watch', function () {
  gulp.watch(paths.sass, ['styles']);
});
