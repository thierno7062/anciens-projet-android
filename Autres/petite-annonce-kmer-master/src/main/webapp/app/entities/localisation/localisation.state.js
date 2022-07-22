(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('localisation', {
            parent: 'entity',
            url: '/localisation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petiteAnnonceKmerApp.localisation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localisation/localisations.html',
                    controller: 'LocalisationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localisation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('localisation-detail', {
            parent: 'entity',
            url: '/localisation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'petiteAnnonceKmerApp.localisation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/localisation/localisation-detail.html',
                    controller: 'LocalisationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('localisation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Localisation', function($stateParams, Localisation) {
                    return Localisation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'localisation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('localisation-detail.edit', {
            parent: 'localisation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localisation/localisation-dialog.html',
                    controller: 'LocalisationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Localisation', function(Localisation) {
                            return Localisation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('localisation.new', {
            parent: 'localisation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localisation/localisation-dialog.html',
                    controller: 'LocalisationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                country: null,
                                region: null,
                                city: null,
                                village: null,
                                area: null,
                                streetName: null,
                                streetNumber: null,
                                postalCode: null,
                                specialAdress: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('localisation', null, { reload: 'localisation' });
                }, function() {
                    $state.go('localisation');
                });
            }]
        })
        .state('localisation.edit', {
            parent: 'localisation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localisation/localisation-dialog.html',
                    controller: 'LocalisationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Localisation', function(Localisation) {
                            return Localisation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('localisation', null, { reload: 'localisation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('localisation.delete', {
            parent: 'localisation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/localisation/localisation-delete-dialog.html',
                    controller: 'LocalisationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Localisation', function(Localisation) {
                            return Localisation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('localisation', null, { reload: 'localisation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
