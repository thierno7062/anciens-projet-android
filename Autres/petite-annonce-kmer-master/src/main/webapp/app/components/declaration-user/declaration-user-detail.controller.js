/**
 * Created by admin on 30/12/2016.
 */


(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('DeclarationUserDetailController', DeclarationUserDetailController);

    DeclarationUserDetailController.$inject = ['declaration','$uibModalInstance','DataUtils','Image'];

    function DeclarationUserDetailController (declaration, $uibModalInstance,DataUtils,Image) {
        var vm = this;

        vm.clear = clear;
        /** onChanges bindings. */
        vm.$onChanges = onChanges;

        vm.declaration = declaration;

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.noWrapSlides = false;
        vm.isCollapsed = true;


        /**
         * Performs when bindings changes.
         * Change read only flag.
         */
        function onChanges() {

        }

        vm.declaration.images = Image.getByDeclaration(declaration);

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
