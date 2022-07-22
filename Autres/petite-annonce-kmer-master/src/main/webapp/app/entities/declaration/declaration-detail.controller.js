(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('DeclarationDetailController', DeclarationDetailController);

    DeclarationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Declaration', 'User', 'Localisation', 'Image'];

    function DeclarationDetailController($scope, $rootScope, $stateParams, previousState, entity, Declaration, User, Localisation, Image) {
        var vm = this;

        vm.declaration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('petiteAnnonceKmerApp:declarationUpdate', function(event, result) {
            vm.declaration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
