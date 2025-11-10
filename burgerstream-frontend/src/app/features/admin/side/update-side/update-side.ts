import { Component } from '@angular/core';
import { SizeOption } from '../../../../shared/models/menu/sizeOption.model';
import { Side } from '../../../../shared/models/menu/side.model';
import { SideService } from '../../../../shared/services/side-service/side-service';
import { SizeOptionService } from '../../../../shared/services/sizeOption-service/size-option-service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'update-side',
  imports: [FormsModule, CommonModule],
  templateUrl: './update-side.html',
  styleUrl: './update-side.css'
})
export class UpdateSide {
    id: number = 0;
    side: Side = new Side('', '', 0, '', []);

    allSizes: SizeOption[] = [];

    selectedSize?: SizeOption;
    selectedfile?: File;

    constructor(private sideService: SideService, private sizeOptionService: SizeOptionService, private router: Router, private route: ActivatedRoute) { }

    ngOnInit(): void {
        this.id = this.route.snapshot.params['id'];
        this.sideService.getSideById(this.id).subscribe(data => {
            this.side = data;

            this.sizeOptionService.getAllSizes().subscribe(
                data => {
                    console.log(data);
                    const selectedIds = this.side.sizeOptions.map(s => s.id);
                    this.allSizes = data.filter(size => !selectedIds.includes(size.id));
                },
                error => console.log("Http error: ", error)
            )
        }, error => console.log(error));
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('side', new Blob([JSON.stringify(this.side)], { type: 'application/json' }));
        if (this.selectedfile) {
            formData.append('image', this.selectedfile);
        }

        this.updateSide(formData);
    }

    updateSide(formData: FormData) {
        if (this.side.id) {
            this.sideService.updateSide(this.side.id, formData).subscribe(data => {
                console.log(formData);
                this.goToAdminView();
            },
                error => console.log(error));
        }
    }

    goToAdminView() {
        this.router.navigate(['/admin']);
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
