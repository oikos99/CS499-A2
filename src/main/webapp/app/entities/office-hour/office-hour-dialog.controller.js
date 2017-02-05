(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('OfficeHourDialogController', OfficeHourDialogController);

    OfficeHourDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OfficeHour'];

    function OfficeHourDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OfficeHour) {
        var vm = this;

        vm.officeHour = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.officeHour.id !== null) {
                OfficeHour.update(vm.officeHour, onSaveSuccess, onSaveError);
            } else {
                OfficeHour.save(vm.officeHour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a2App:officeHourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
