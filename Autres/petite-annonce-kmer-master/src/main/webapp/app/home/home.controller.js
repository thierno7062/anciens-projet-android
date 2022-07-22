(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','DeclarationUserService','Declaration','$q'];

    function HomeController ($scope, Principal, LoginService, $state,DeclarationUserService,Declaration,$q) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.openDeclaration = openDeclaration;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function openDeclaration() {
            DeclarationUserService.openNew();
        }

        vm.initMap = function() {
            // Prepare demo data

            var regionCode =
             [  "cm-es",
                "cm-ad",
                "cm-nw",
                "cm-no",
                "cm-ce",
                "cm-ou",
                "cm-en",
                "cm-sw",
                "cm-lt",
                "cm-su"

            ];

            var promise = {};
            var promises = [];

           angular.forEach(regionCode, function (value) {
             var promise =   Declaration.countAllPerRegion({IdRegion:value}).$promise;
                promises.push(promise);
            });

            $q.all(promises).then(function(results) {

                var data = [];
                angular.forEach(results, function (value) {
                        data.push({
                            'hc-key':value.headers['x-region-code'],
                            'value':value.headers['x-total-count']
                        });
                });
                // Initiate the chart
                $('#camerounmap').highcharts('Map',
                    {

                        title: {
                            text: 'Choisissez une region'
                        },

                        subtitle: {
                            text: 'pour trouvez un objet'
                        },

                        chart:{
                            backgroundColor:'#f5f5f5'
                        },

                        plotOptions: {
                            series: {
                                point: {
                                    events: {
                                        click: function (e) {
                                            $state.go('region-declaration',
                                                {regionId: e.point['hc-key'],
                                                    regionName: e.point.name}
                                            );
                                        }
                                    }
                                }
                            }
                        },


                        colorAxis: {
                            min: 0,
                            max: 60,
                            maxColor: "#081c2a"
                        },

                        series: [{
                            data: data,
                            mapData: Highcharts.maps['countries/cm/cm-all'],
                            joinBy: 'hc-key',
                            name: "annonce publiées",
                            allowPointSelect: true,
                            states: {
                                hover: {
                                    color: '#8e44ad',
                                    borderColor: 'black'
                                },
                                select: {
                                    color: '#8e44ad',
                                    borderColor: 'black'
                                }
                            },
                            dataLabels: {
                                enabled: true,
                                format: '{point.name}'
                            }
                        }, {
                            type: 'mappoint',
                            id: 'clicks',
                            data: []
                        }],
                        legend: {
                            enabled: false
                        },
                        exporting: {
                            enabled: false
                        },
                        credits: {
                            enabled:false
                        }


                    });
            });


        };
        vm.initMap();

    }
})();
