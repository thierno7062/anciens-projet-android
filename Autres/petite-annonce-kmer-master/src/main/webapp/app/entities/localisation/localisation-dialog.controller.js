(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('LocalisationDialogController', LocalisationDialogController);

    LocalisationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Localisation', 'User','Country','Region'];

    function LocalisationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Localisation, User,Country,Region) {
        var vm = this;

        vm.localisation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.loadRegion = loadRegion;
        vm.users = User.query();
        vm.countries = Country.query();


        vm.countryRegion = {};

        vm.loadRegion();


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.localisation.id !== null) {
                Localisation.update(vm.localisation, onSaveSuccess, onSaveError);
            } else {
                Localisation.save(vm.localisation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('petiteAnnonceKmerApp:localisationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function loadRegion() {
            if(vm.localisation.country) {
                vm.countryRegion = Region.getByCountry(
                    {countryId: vm.localisation.country.id}
                )
            }
        }

    }
})();
