(function() {
    'use strict';
    angular
        .module('petiteAnnonceKmerApp')
        .factory('Localisation', Localisation);

    Localisation.$inject = ['$resource'];

    function Localisation ($resource) {
        var resourceUrl =  'api/localisations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
