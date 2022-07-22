(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('DeclarationDialogController', DeclarationDialogController);

    DeclarationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Declaration', 'User', 'Localisation', 'Image'];

    function DeclarationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Declaration, User, Localisation, Image) {
        var vm = this;

        vm.declaration = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.localisations = Localisation.query({filter: 'declaration-is-null'});
        $q.all([vm.declaration.$promise, vm.localisations.$promise]).then(function() {
            if (!vm.declaration.localisation || !vm.declaration.localisation.id) {
                return $q.reject();
            }
            return Localisation.get({id : vm.declaration.localisation.id}).$promise;
        }).then(function(localisation) {
            vm.localisations.push(localisation);
        });
        vm.images = Image.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.declaration.id !== null) {
                Declaration.update(vm.declaration, onSaveSuccess, onSaveError);
            } else {
                Declaration.save(vm.declaration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('petiteAnnonceKmerApp:declarationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.lastModifiedDate = false;
        vm.datePickerOpenStatus.publishedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
