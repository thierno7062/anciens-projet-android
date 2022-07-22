(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('LocalisationController', LocalisationController);

    LocalisationController.$inject = ['$scope', '$state', 'Localisation'];

    function LocalisationController ($scope, $state, Localisation) {
        var vm = this;

        vm.localisations = [];

        loadAll();

        function loadAll() {
            Localisation.query(function(result) {
                vm.localisations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
