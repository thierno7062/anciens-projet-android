(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('ImageDetailController', ImageDetailController);

    ImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Image', 'Declaration'];

    function ImageDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Image, Declaration) {
        var vm = this;

        vm.image = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('petiteAnnonceKmerApp:imageUpdate', function(event, result) {
            vm.image = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
