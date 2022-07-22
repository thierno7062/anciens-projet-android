/**
 * Created by admin on 12/12/2016.
 * controler for the user creation of a new declration
 */



(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('DeclarationUserController', DeclarationUserController);

    DeclarationUserController.$inject = ['$scope', '$uibModalInstance', 'entity', 'DeclarationUser', 'User','Country','Region', 'LoginService', 'Principal'];

    function DeclarationUserController ($scope, $uibModalInstance, entity, DeclarationUser, User,Country,Region, LoginService,Principal) {
        var vm = this;

        vm.declaration = entity;
        vm.clear = clear;
        vm.onSaveSuccess = onSaveSuccess;
        vm.onSaveError = onSaveError;
        vm.generalSave = generalSave;


        // register field
        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;

        vm.registerAccount = {};
        vm.success = null;
        vm.isSave = false;
        vm.currentAccount = {};
        vm.loginExist = false;


        vm.isAuthenticated = Principal.isAuthenticated();
       if(vm.isAuthenticated){
           Principal.identity().then(function(account) {
               vm.currentAccount = account;
           });
       }else {
           // verify that the typed login exist
           $scope.$watch('vm.registerAccount.login', function (typedLogin) {
               if (typedLogin) {
                   vm.loginExists(typedLogin);
               }
           });
       }


        /**
         * verify if login exists
          * @param login
         */
       function loginExists(login) {

           User.loginExist({
               login:login
           },function (data) {
               vm.loginExist = true;
           },function (error) {
               vm.loginExist = false;
           });
       }





        vm.select1 = select1;
        vm.select2 = select2;
        vm.select3 = select3;
        vm.loadRegion = loadRegion;

         Country.query(function (data) {
             vm.countries = data;
             vm.localisation.country =vm.countries[0]; //Cameroun is the first country
             vm.loadRegion();
        });
        vm.countryRegion = {};
        vm.localisation = {};

        vm.images = {};

        vm.images.principal = {}
        vm.images.image2 = {}
        vm.images.image3 = {}
        vm.hideUpload1 = false;
        vm.hideUpload2 = false;
        vm.hideUpload3 = false;


        function select1(file) {
            console.log(file);
            console.log(vm.images.principal);
            vm.hideUpload1 = true;
        }
        function select2(file) {
            console.log(file);
            vm.hideUpload2 = true;
        }

        function select3(file) {
            console.log(file);
            vm.hideUpload3 = true;
        }

     // cameroun cities by google places options
        vm.citiesOptions = {
            country: 'cm',
            types: '(cities)'
        };
        vm.citiesOptionsDetail ='';



        function clear () {
            $uibModalInstance.dismiss('cancel');
        }


        function onSaveSuccess (data) {
            vm.isSave = true;
        }

        function onSaveError () {
            console.log("saved Error");
            vm.isSave = false;
        }

        function loadRegion() {
            if(vm.localisation.country) {
                vm.countryRegion = Region.getByCountry(
                    {countryId: vm.localisation.country.id}
                )
            }
        }



        function generalSave(login) {
            console.log("generalSave "+login);
            vm.isAuthenticated = Principal.isAuthenticated();

            if(vm.isAuthenticated){
                DeclarationUser.saveDeclarationUser(
                    vm.declaration,
                    vm.localisation,
                    login,
                    [vm.images.principal,vm.images.image2,vm.images.image3]
                ).then(onSaveSuccess)
                    .catch(onSaveError)

            }else{
                var  declarationParams = {
                    data: vm.declaration,
                    localisation: vm.localisation,
                    images:[vm.images.principal,vm.images.image2,vm.images.image3]
                }
                LoginService.open(true,$uibModalInstance,declarationParams);
            }
        }
        /* if login success save the declaration ie onLoginSuccessSaveStartedDeclarationCreation*/
        $scope.$on("onLoginSuccessSaveStartedDeclarationCreation",function(event, args) {
            var login = args.login;
            console.log("$on "+login);
            vm.generalSave(login);
        });

    }
})();
