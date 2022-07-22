/**
 * Created by admin on 10/12/2016.
 */

(function () {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('Region', Region);

    Region.$inject = ['$resource'];

    function Region ($resource) {
        var service = $resource('api/region/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'getByCountry': {
                url:'api/region/getByCountry/:countryId',
                method: 'GET',
                isArray: true
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
