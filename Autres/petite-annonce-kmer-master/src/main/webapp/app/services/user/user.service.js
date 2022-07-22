(function () {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
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
            'loginExist': { url:'/api/account/loginExist', method:'GET',
                transformResponse: function(data, headers,statusCode) {
                    var finalRsponse = {
                        data: data,
                        responseStatusCode: statusCode
                    };
                    return statusCode;
                }
            },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();
