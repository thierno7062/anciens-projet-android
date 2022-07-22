(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$state', '$timeout', 'Auth', '$uibModalInstance','isSaveDeclaration','declarationUibModalInstance','declaration'];

    function LoginController ($rootScope, $state, $timeout, Auth, $uibModalInstance,isSaveDeclaration,declarationUibModalInstance,declaration) {
        var vm = this;

        // flag test whether login is coming from new declaration
        vm.isSaveDeclaration = isSaveDeclaration;

        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.register = register;
        vm.rememberMe = true;
        vm.requestResetPassword = requestResetPassword;
        vm.username = null;

        $timeout(function (){angular.element('#username').focus();});

        function cancel () {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
            $uibModalInstance.dismiss('cancel');
        }

        function login (event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                $uibModalInstance.close();
                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }

                $rootScope.$broadcast('authenticationSuccess');

                // event to test whether login is coming from new declaration
                if(vm.isSaveDeclaration){
                    console.log("onLoginSuccessSaveStartedDeclarationCreation");
                    $rootScope.$broadcast('onLoginSuccessSaveStartedDeclarationCreation',
                        {
                        login:vm.username
                        } );
                }

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is successful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }
            }).catch(function () {
                vm.authenticationError = true;
            });
        }

        function register () {
            $uibModalInstance.dismiss('cancel');
            $state.go('register');
            if(vm.isSaveDeclaration) {

                vm.declarationUibModalInstance = declarationUibModalInstance;

                // if login is coming from declaratoin creation
                //save declaration if account creation successful
                vm.declaration = declaration;

                vm.declarationUibModalInstance.dismiss('cancel');
                var declarationParams  = { declaration:vm.declaration };
                $state.go('register',declarationParams);
            }else{
                $state.go('register');
            }
        }

        function requestResetPassword () {
            $uibModalInstance.dismiss('cancel');
            $state.go('requestReset');
        }
    }
})();
