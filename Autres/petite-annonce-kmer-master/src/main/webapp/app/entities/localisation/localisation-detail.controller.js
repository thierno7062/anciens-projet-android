(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('LocalisationDetailController', LocalisationDetailController);

    LocalisationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Localisation', 'User'];

    function LocalisationDetailController($scope, $rootScope, $stateParams, previousState, entity, Localisation, User) {
        var vm = this;

        vm.localisation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('petiteAnnonceKmerApp:localisationUpdate', function(event, result) {
            vm.localisation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
