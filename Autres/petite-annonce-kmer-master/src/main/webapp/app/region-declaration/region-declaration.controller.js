(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('RegionDeclarationController', RegionDeclarationController);

    RegionDeclarationController.$inject = ['AlertService','Declaration','region', 'ParseLinks','paginationConstants', 'pagingParams','$state','Country','Region','$scope'];

    function RegionDeclarationController (AlertService, Declaration, region, ParseLinks,paginationConstants, pagingParams,$state,Country,Region, $scope) {
        var vm = this;
        vm.declarations = [];
        vm.isReload = false;
        vm.currentSearch = "";
        vm.loadAll = loadAll;

        vm.loadPage = loadPage;
        vm.transition = transition;

        vm.loadRegion = loadRegion;
        vm.localisation = {};
        vm.localisation.region = {
            "name" : region.regionName,
            "code" : region.regionId
        };

        vm.regionName = vm.localisation.region.name;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.onChangeSelect = onChangeSelect;

        vm.loadAll();

        /**
         * get all countries and select the first (Cameroun)
         */

        Country.query(function (data) {
            vm.countries = data;
            vm.localisation.country =vm.countries[0]; //Cameroun is the first country
            vm.loadRegion();
        });

        /**
         * load all declarations for a region
         */
        function loadAll() {
            Declaration.getAllDeclarationsByRegion({
                        IdRegion:vm.localisation.region.code,
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage,
                        sort: sort(),
                        search: vm.currentSearch
                }
                , onSuccess, onError);
        }


        /**
         * sort declaration list
         * @returns {[*]}
         */
        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.declarations = data;
            vm.page = pagingParams.page;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                regionId: vm.localisation.region.code,
                regionName: vm.localisation.region.name,
                search: vm.currentSearch
            });
        }

        function loadRegion() {
            if(vm.localisation.country) {
                vm.countryRegion = Region.getByCountry(
                    {countryId: vm.localisation.country.id}
                )
            }
        }

        /**
         * on select region display declaration of that region if reload is active
         */
        function onChangeSelect() {
            if(vm.isReload){
                vm.loadAll();
                vm.regionName = vm.localisation.region.name;
            }
        }

        $scope.$watch('vm.currentSearch', function(value) {
            vm.onChangeSelect();
        }, true);

    }
})();
