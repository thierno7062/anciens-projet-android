/**
 * Created by admin on 29/12/2016.
 */
(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('settings.my-declarations', {
            parent: 'settings',
            url: '/my-declarations?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.myDeclarations'
            },
            views: {
                'settingcontent@settings': {
                    templateUrl: 'app/account/my-declarations/my-declarations.html',
                    controller: 'MyDeclarationsController',
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
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('password');
                    return $translate.refresh();
                }],
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        });
    }
})();
