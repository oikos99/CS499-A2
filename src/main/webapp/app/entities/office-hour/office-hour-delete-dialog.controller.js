(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('OfficeHourDeleteController',OfficeHourDeleteController);

    OfficeHourDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfficeHour'];

    function OfficeHourDeleteController($uibModalInstance, entity, OfficeHour) {
        var vm = this;

        vm.officeHour = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfficeHour.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
