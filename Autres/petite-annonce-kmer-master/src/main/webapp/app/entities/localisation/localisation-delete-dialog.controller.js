(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('LocalisationDeleteController',LocalisationDeleteController);

    LocalisationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Localisation'];

    function LocalisationDeleteController($uibModalInstance, entity, Localisation) {
        var vm = this;

        vm.localisation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Localisation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
