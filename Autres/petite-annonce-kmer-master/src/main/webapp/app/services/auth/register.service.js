(function () {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
