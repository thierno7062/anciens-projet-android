(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('ImageDialogController', ImageDialogController);

    ImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Image', 'Declaration'];

    function ImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Image, Declaration) {
        var vm = this;

        vm.image = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.declarations = Declaration.query({
            IdRegion:"gaetan"
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.image.id !== null) {
                Image.update(vm.image, onSaveSuccess, onSaveError);
            } else {
                Image.save(vm.image, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('petiteAnnonceKmerApp:imageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setContent = function ($file, image) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        image.content = base64Data;
                        image.contentContentType = $file.type;
                    });
                });
            }
        };

    }
})();
