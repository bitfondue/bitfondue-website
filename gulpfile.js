var gulp = require('gulp'),
    concat = require('gulp-concat');

var paths = {
  css: 'resources/css/*.css'
}

gulp.task('styles', function () {
  gulp.src([
    'bower_components/bootstrap/dist/css/bootstrap.min.css',
    paths.css
  ])
  .pipe(concat('app.min.css'))
  .pipe(gulp.dest('resources/public/css'));
});

gulp.task('default', ['styles']);

gulp.task('watch', function () {
  gulp.watch(paths.css, ['styles']);
});
