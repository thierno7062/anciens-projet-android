(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('settings.password', {
            parent: 'settings',
            url: '/password',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.account.password'
            },
            views: {
                'settingcontent@settings': {
                    templateUrl: 'app/account/password/password.html',
                    controller: 'PasswordController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('password');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
