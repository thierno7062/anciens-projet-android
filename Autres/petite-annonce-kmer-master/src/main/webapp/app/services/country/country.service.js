/**
 * Created by admin on 10/12/2016.
 */

(function () {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('Country', Country);

    Country.$inject = ['$resource'];

    function Country ($resource) {
        var service = $resource('api/country/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();

