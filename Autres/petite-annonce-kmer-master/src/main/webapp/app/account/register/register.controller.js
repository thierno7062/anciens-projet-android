(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService','DeclarationUser','$stateParams'];

    function RegisterController ($translate, $timeout, Auth, LoginService,DeclarationUser,$stateParams) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.success = null;
        vm.declaration = $stateParams.declaration;
        vm.isDeclarationSaveSuccess = false;
        vm.isDeclarationSaveFail = false;

        $timeout(function (){angular.element('#login').focus();});

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function () {
                    vm.success = 'OK';
                    if(vm.declaration != null){
                        console.log(vm.declaration)

                        DeclarationUser.saveDeclarationUser(
                            vm.declaration.data,
                            vm.declaration.localisation,
                            vm.registerAccount.login,
                            vm.declaration.images
                        ).then(function () {
                            vm.isDeclarationSaveSuccess = true;
                            vm.isDeclarationSaveFail = false;
                        })
                        .catch(function () {
                            vm.isDeclarationSaveSuccess = false;
                            vm.isDeclarationSaveFail = true;
                            })
                    }

                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }
    }
})();
