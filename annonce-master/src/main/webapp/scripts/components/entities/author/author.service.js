'use strict';

angular.module('newsappApp')
    .factory('Author', function ($resource) {
        return $resource('api/authors/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
