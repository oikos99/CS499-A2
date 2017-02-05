(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('OfficeHourController', OfficeHourController);

    OfficeHourController.$inject = ['$scope', '$state', 'OfficeHour'];

    function OfficeHourController ($scope, $state, OfficeHour) {
        var vm = this;

        vm.officeHours = [];

        loadAll();

        function loadAll() {
            OfficeHour.query(function(result) {
                vm.officeHours = result;
                vm.searchQuery = null;
            });
        }
    }
})();
