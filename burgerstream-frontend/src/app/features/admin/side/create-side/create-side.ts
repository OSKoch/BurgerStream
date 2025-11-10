import { Component, OnInit } from '@angular/core';
import { Side } from '../../../../shared/models/menu/side.model';
import { SideService } from '../../../../shared/services/side-service/side-service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SizeOptionService } from '../../../../shared/services/sizeOption-service/size-option-service';
import { SizeOption } from '../../../../shared/models/menu/sizeOption.model';

@Component({
    selector: 'create-side',
    imports: [FormsModule, CommonModule],
    templateUrl: './create-side.html',
    styleUrl: './create-side.css'
})
export class CreateSide implements OnInit {

    side: Side = new Side('', '', 0, '', []);

    selectedfile?: File;

    selectedSize?: SizeOption;

    allSizes: SizeOption[] = [];

    constructor(private sideService: SideService, private sizeOptionService: SizeOptionService, private router: Router) { }

    ngOnInit(): void {
        this.getSizes();

    }

    private getSizes() {
        this.sizeOptionService.getAllSizes().subscribe(
            data => {
                console.log(data);
                this.allSizes = data;
            },
            error => console.log("Http error: ", error)
        )
    }

    goToAdminView() {
        this.router.navigate(['/admin']);
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('side', new Blob([JSON.stringify(this.side)], { type: 'application/json' }));
        if (this.selectedfile) {
            formData.append('image', this.selectedfile);
        }

        this.saveSide(formData);
    }

    saveSide(formData: FormData) {
        this.sideService.createSide(formData).subscribe(data => {
            console.log(data)
            this.goToAdminView();
        },
            error => console.log(error));
    }

    addSizeToSide(size: SizeOption) {
        const alreadyAdded = this.side.sizeOptions.some(s => s.id === size.id);
        if (alreadyAdded) return;

        this.side.sizeOptions.push(size);

        this.allSizes = this.allSizes.filter(s => s.id !== size.id);
    }

    removeSizeFromSide(size: SizeOption) {
        this.side.sizeOptions = this.side.sizeOptions.filter(s => s.id !== size.id);

        const existsInAllSizes = this.allSizes.some(s => s.id === size.id);
        if (!existsInAllSizes) {
            this.allSizes.push(size);
        }
    }

    onFileSelected(event: any) {
        this.selectedfile = event.target.files[0];
    }
}
