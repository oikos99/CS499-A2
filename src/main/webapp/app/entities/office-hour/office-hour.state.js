(function() {
    'use strict';

    angular
        .module('a2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('office-hour', {
            parent: 'entity',
            url: '/office-hour',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a2App.officeHour.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/office-hour/office-hours.html',
                    controller: 'OfficeHourController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('officeHour');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('office-hour-detail', {
            parent: 'entity',
            url: '/office-hour/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a2App.officeHour.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/office-hour/office-hour-detail.html',
                    controller: 'OfficeHourDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('officeHour');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OfficeHour', function($stateParams, OfficeHour) {
                    return OfficeHour.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'office-hour',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('office-hour-detail.edit', {
            parent: 'office-hour-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office-hour/office-hour-dialog.html',
                    controller: 'OfficeHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfficeHour', function(OfficeHour) {
                            return OfficeHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('office-hour.new', {
            parent: 'office-hour',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office-hour/office-hour-dialog.html',
                    controller: 'OfficeHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('office-hour', null, { reload: 'office-hour' });
                }, function() {
                    $state.go('office-hour');
                });
            }]
        })
        .state('office-hour.edit', {
            parent: 'office-hour',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office-hour/office-hour-dialog.html',
                    controller: 'OfficeHourDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfficeHour', function(OfficeHour) {
                            return OfficeHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('office-hour', null, { reload: 'office-hour' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('office-hour.delete', {
            parent: 'office-hour',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/office-hour/office-hour-delete-dialog.html',
                    controller: 'OfficeHourDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfficeHour', function(OfficeHour) {
                            return OfficeHour.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('office-hour', null, { reload: 'office-hour' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
