(function() {
    'use strict';
    angular
        .module('petiteAnnonceKmerApp')
        .factory('Declaration', Declaration)
        .factory('DeclarationUser', DeclarationUser);

    Declaration.$inject = ['$resource', 'DateUtils'];
    DeclarationUser.$inject = ['$resource', 'DateUtils', 'Upload'];

    function Declaration ($resource, DateUtils) {
        var resourceUrl =  'api/declarations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.lastModifiedDate = DateUtils.convertDateTimeFromServer(data.lastModifiedDate);
                        data.publishedDate = DateUtils.convertDateTimeFromServer(data.publishedDate);
                    }
                    return data;
                }
            },
            'getAllDeclarationsByRegion': {
                url:'api/declarations-byregion',
                method: 'GET',
                isArray: true
            },

            'countAllPerRegion': {
                url:'api/declarations-byregion-count/:IdRegion',
                method: 'GET',
                transformResponse: function(data, headers){
                    var response = {}
                    response.data = data;
                    response.headers = headers();
                    return response;
                }
            },

            'update': { method:'PUT' }
        });
    }

    function DeclarationUser ($resource, DateUtils,Upload){

        return{
            saveDeclarationUser:saveDeclarationUser
        }

        function saveDeclarationUser(declaration,localisation,login, images) {

            return Upload.upload({
                url: 'api/save-declarations-user',
                data: {
                    declaration: new Blob(
                        [JSON.stringify(declaration)],
                        {type: 'application/json'}),
                    localisation: new Blob(
                        [JSON.stringify(localisation)],
                        {type: 'application/json'}),
                    login:login,
                    images:images
                },
                arrayKey: ''
            });

        }

    }



})();
