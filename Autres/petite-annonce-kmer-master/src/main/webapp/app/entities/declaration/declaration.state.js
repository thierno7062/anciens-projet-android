(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('declaration', {
            parent: 'entity',
            url: '/declaration?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petiteAnnonceKmerApp.declaration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/declaration/declarations.html',
                    controller: 'DeclarationController',
                    controllerAs: 'vm'
                }
            },
            params: {
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
                    $translatePartialLoader.addPart('declaration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('declaration-detail', {
            parent: 'entity',
            url: '/declaration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petiteAnnonceKmerApp.declaration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/declaration/declaration-detail.html',
                    controller: 'DeclarationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('declaration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Declaration', function($stateParams, Declaration) {
                    return Declaration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'declaration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('declaration-detail.edit', {
            parent: 'declaration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/declaration/declaration-dialog.html',
                    controller: 'DeclarationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Declaration', function(Declaration) {
                            return Declaration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('declaration.new', {
            parent: 'declaration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/declaration/declaration-dialog.html',
                    controller: 'DeclarationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                creationDate: null,
                                lastModifiedDate: null,
                                isPublished: false,
                                price: null,
                                publishedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('declaration', null, { reload: 'declaration' });
                }, function() {
                    $state.go('declaration');
                });
            }]
        })
        .state('declaration.edit', {
            parent: 'declaration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/declaration/declaration-dialog.html',
                    controller: 'DeclarationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Declaration', function(Declaration) {
                            return Declaration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('declaration', null, { reload: 'declaration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('declaration.delete', {
            parent: 'declaration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/declaration/declaration-delete-dialog.html',
                    controller: 'DeclarationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Declaration', function(Declaration) {
                            return Declaration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('declaration', null, { reload: 'declaration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
