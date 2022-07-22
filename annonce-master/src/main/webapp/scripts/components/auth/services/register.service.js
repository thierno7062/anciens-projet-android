'use strict';

angular.module('newsappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


