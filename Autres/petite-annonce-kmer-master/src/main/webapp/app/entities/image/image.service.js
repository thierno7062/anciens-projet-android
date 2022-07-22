(function() {
    'use strict';
    angular
        .module('petiteAnnonceKmerApp')
        .factory('Image', Image);

    Image.$inject = ['$resource'];

    function Image ($resource) {
        var resourceUrl =  'api/images/:id';

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
            'update': { method:'PUT' },
            'getByDeclaration': {
                                  method:'GET',
                                  url:'api/declaration-images/:id',
                                  isArray: true
                                }
        });
    }
})();
