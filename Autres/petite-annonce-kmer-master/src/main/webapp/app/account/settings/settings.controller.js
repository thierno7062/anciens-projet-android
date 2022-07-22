(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['DeclarationUserService','$state','Principal', 'Auth', 'JhiLanguageService', '$translate'];

    function SettingsController (DeclarationUserService, $state,Principal, Auth, JhiLanguageService, $translate) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.status = false;
        vm.openDeclaration = openDeclaration;
        vm.logout = logout;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                phoneNumber:account.phoneNumber,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

        function logout() {
            Auth.logout();
            $state.go('home');
        }

        function openDeclaration() {
            DeclarationUserService.openNew();
        }

    }
})();
