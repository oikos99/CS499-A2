(function() {
    'use strict';

    angular
        .module('a2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('instructor', {
            parent: 'entity',
            url: '/instructor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a2App.instructor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instructor/instructors.html',
                    controller: 'InstructorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instructor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('instructor-detail', {
            parent: 'entity',
            url: '/instructor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'a2App.instructor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/instructor/instructor-detail.html',
                    controller: 'InstructorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('instructor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Instructor', function($stateParams, Instructor) {
                    return Instructor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'instructor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('instructor-detail.edit', {
            parent: 'instructor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instructor/instructor-dialog.html',
                    controller: 'InstructorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Instructor', function(Instructor) {
                            return Instructor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instructor.new', {
            parent: 'instructor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instructor/instructor-dialog.html',
                    controller: 'InstructorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('instructor', null, { reload: 'instructor' });
                }, function() {
                    $state.go('instructor');
                });
            }]
        })
        .state('instructor.edit', {
            parent: 'instructor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instructor/instructor-dialog.html',
                    controller: 'InstructorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Instructor', function(Instructor) {
                            return Instructor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instructor', null, { reload: 'instructor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('instructor.delete', {
            parent: 'instructor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/instructor/instructor-delete-dialog.html',
                    controller: 'InstructorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Instructor', function(Instructor) {
                            return Instructor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('instructor', null, { reload: 'instructor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
