(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('OfficeHourDetailController', OfficeHourDetailController);

    OfficeHourDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'OfficeHour'];

    function OfficeHourDetailController($scope, $rootScope, $stateParams, previousState, entity, OfficeHour) {
        var vm = this;

        vm.officeHour = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('a2App:officeHourUpdate', function(event, result) {
            vm.officeHour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
