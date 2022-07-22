(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('region-declaration', {
            parent: 'app',
            url: '/region-declaration/:regionName/:regionId?page&sort&search',
            data: {
                authorities: [],
                pageTitle: 'petiteAnnonceKmerApp.localisation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/region-declaration/region-declaration.html',
                    controller: 'RegionDeclarationController',
                    controllerAs: 'vm'
                }
            }, params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                region: function ($stateParams) {

                    return {
                        regionId: $stateParams.regionId,
                        regionName: $stateParams.regionName
                    };
                },
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('declaration-user');
                    $translatePartialLoader.addPart('register');
                    $translatePartialLoader.addPart('region');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        }).state('declaration-detail-region', {
            parent: 'region-declaration',
            url: '/declaration-detail-region/:declarationId',
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    animation: true,
                    templateUrl: 'app/components/declaration-user/declaration-user-detail.html',
                    controller: 'DeclarationUserDetailController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        declaration: ['Declaration', function(Declaration) {
                            return Declaration.get({id : $stateParams.declarationId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        });

    }

})();
