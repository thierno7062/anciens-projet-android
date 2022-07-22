(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('DeclarationDeleteController',DeclarationDeleteController);

    DeclarationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Declaration'];

    function DeclarationDeleteController($uibModalInstance, entity, Declaration) {
        var vm = this;

        vm.declaration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Declaration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
