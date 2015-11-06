var gulp = require('gulp'),
    concat = require('gulp-concat');

gulp.task('styles', function () {
  gulp.src([
    'bower_components/bootstrap/dist/css/bootstrap.min.css'
  ])
  .pipe(concat('deps.css'))
  .pipe(gulp.dest('resources/public/css'));
});

gulp.task('default', ['styles']);
